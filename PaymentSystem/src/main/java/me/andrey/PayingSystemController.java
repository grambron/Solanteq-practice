package me.andrey;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: Add tests

@Controller
public class PayingSystemController {


    private final int binLength = 6;
    private final IssuerBankRepository issuerBankRepository;

    public PayingSystemController(IssuerBankRepository issuerBankRepository) {
        this.issuerBankRepository = issuerBankRepository;
    }



    @PostMapping("/redirect")
    public ResponseEntity<?> redirectTransactionToIssuer(@RequestBody Map<String, String> body) {
        String cardNum = body.get("num");
        String bin = Utils.getBinFromCardNum(cardNum);
        String urlString = issuerBankRepository.findByBin(bin).getUrlString();

        try {
            return sendTransactionToIssuerBan(urlString, new JSONObject(body));
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    //TODO: Add status output in html
    @PostMapping("/add")
    public ResponseEntity<String> addIssuerBank(@RequestBody Map<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("response:", "edit-result");

        String newBin = body.get("newBin");
        String newUrl = body.get("newUrl");

        if (newBin == null || newUrl == null) {
            return new ResponseEntity<>("Incorrect json", headers, HttpStatus.OK);
        }

        if (newBin.length() != binLength) {
            return new ResponseEntity<>("Incorrect data", headers, HttpStatus.BAD_REQUEST);
        }

        try {
            Integer.parseInt(newBin);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Incorrect data", headers, HttpStatus.BAD_REQUEST);
        }

        IssuerBank newBank = new IssuerBank(newBin, newUrl);
        if (issuerBankRepository.findByBin(newBin) != null) {
            return new ResponseEntity<>("Bin already exists", headers, HttpStatus.BAD_REQUEST);
        }

        issuerBankRepository.save(newBank);
        return new ResponseEntity<>("Done", headers, HttpStatus.OK);
    }

    //TODO: Add status output in html
    @PostMapping("/edit")
    public ResponseEntity<String> editIssuerBank(@RequestBody Map<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("response:", "edit-result");

        String deleteBin = body.get("delBin");
        String newBin = body.get("newBin");
        String newUrl = body.get("newUrl");
        if (deleteBin == null || newBin == null || newUrl == null) {
            return new ResponseEntity<>("Incorrect json", headers, HttpStatus.OK);
        }

        if (deleteBin.length() != binLength || newBin.length() != binLength) {
            return new ResponseEntity<>("Incorrect data", headers, HttpStatus.BAD_REQUEST);
        }

        try {
            Integer.parseInt(deleteBin);
            Integer.parseInt(newBin);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Incorrect data", headers, HttpStatus.BAD_REQUEST);
        }

        IssuerBank bankToDelete;
        try {
            bankToDelete = issuerBankRepository.findByBin(deleteBin);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Incorrect database state", headers, HttpStatus.NOT_FOUND);
        }

        if (bankToDelete == null) {
            return new ResponseEntity<>("Not found", headers, HttpStatus.NOT_FOUND);
        }

        try {
            issuerBankRepository.delete(bankToDelete);
            if (issuerBankRepository.findByBin(newBin) != null) {
                issuerBankRepository.save(bankToDelete);
                return new ResponseEntity<>("Bin already exists", headers, HttpStatus.BAD_REQUEST);
            }
            issuerBankRepository.save(new IssuerBank(newBin, newUrl));
        } catch (Exception e) {
            return new ResponseEntity<>("Error", headers, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Done", headers, HttpStatus.OK);
    }

    private void printBanks() {
        List<String> lst = issuerBankRepository.findAll().stream().
                map(IssuerBank::toCompactString).collect(Collectors.toCollection(ArrayList::new));
        for (String data : lst) {
            System.out.println(data);
        }
    }

    //TODO: Beatify with css
    @GetMapping("/add")
    public String openDBAddPage(Model model) {
        model.addAttribute("bank", new IssuerBank());
        return "add";
    }

    //TODO: Beatify with css
    //TODO: Fix incorrect behaving
    @GetMapping("/edit")
    public String openDBEditPage(Model model) {
        List<IssuerBank> allBanks = issuerBankRepository.findAll();
        model.addAttribute("allBanks", allBanks);
        model.addAttribute("bankToDel", new IssuerBank());
        model.addAttribute("bankToAdd", new IssuerBank());
        return "edit";
    }

    @PostMapping("/rem")
    public void deleteAll() {
        for (IssuerBank ib : issuerBankRepository.findAll()) {
            issuerBankRepository.delete(ib);
        }
    }

    private ResponseEntity<?> sendTransactionToIssuerBan(String urlString, JSONObject body) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(body.toString().getBytes(StandardCharsets.UTF_8));
            wr.flush();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }
}
