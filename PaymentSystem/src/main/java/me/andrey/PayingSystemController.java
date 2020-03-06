package me.andrey;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

//TODO: Add tests

@Controller
public class PayingSystemController {

    final IssuerBankRepository issuerBankRepository;

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
    //TODO: Add redirection to index
    @PostMapping("/add")
    public void addIssuerBank(@ModelAttribute("bank") IssuerBank bank) {
        issuerBankRepository.save(bank);
    }

    //TODO: Add status output in html
    //TODO: Add redirection to index
    @PostMapping("/edit")
    public void editIssuerBank(@ModelAttribute("bankToDel") IssuerBank bankToDel,
                               @ModelAttribute("bankToAdd") IssuerBank bankToAdd) {
        issuerBankRepository.delete(bankToDel);
        issuerBankRepository.save(bankToAdd);
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


    private ResponseEntity<?> sendTransactionToIssuerBan(String urlString, JSONObject body) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(body.toString().getBytes("utf-8"));
            wr.flush();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }
}
