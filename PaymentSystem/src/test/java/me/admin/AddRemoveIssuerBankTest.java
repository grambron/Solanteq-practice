package me.admin;

import org.junit.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AddRemoveIssuerBankTest {
    private PayingSystemController psk;
    private final static List<IssuerBank> expected = new ArrayList<>();

    @Before
    public void setUp() {
        IssuerBank ib0 = new IssuerBank("000000", "url0");
        IssuerBank ib1 = new IssuerBank("000001", "url1");
        IssuerBank ib2 = new IssuerBank("000002", "url2");
        IssuerBank ib3 = new IssuerBank("000003", "url3");
        expected.add(ib0);
        expected.add(ib1);
        expected.add(ib2);
        expected.add(ib3);
        IssuerBankRepositoryExtended testRep = new IssuerBankRepositoryExtended();
        testRep.setUpTable(new ArrayList<>(expected));

        psk = new PayingSystemController(testRep);
    }

    @After
    public void tearDown() {
        psk = null;
        expected.clear();
    }

    @Test
    public void getAllBanks() {
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addOne() {
        String newBin = "000005";
        String oldBin = "";
        String newUrl = "url5";
        IssuerBank exp = new IssuerBank(newBin, newUrl);
        expected.add(exp);
        psk.addIssuerBank(makeBody(newBin, oldBin, newUrl));

        List<IssuerBank> actual = psk.getAllBanks();
        assertEquals(5, actual.size());
        assertEquals(strings(expected), strings(actual));

    }

    @Test
    public void addMany() {
        int cnt = 6;
        for (int i = 4; i < cnt + 4; i++) {
            String newBin = "00000" + i;
            String oldBin = "";
            String newUrl = "url" + i;
            IssuerBank exp = new IssuerBank(newBin, newUrl);
            expected.add(exp);

            psk.addIssuerBank(makeBody(newBin, oldBin, newUrl));
        }
        List<IssuerBank> actual = psk.getAllBanks();
//        actual.removeAll(expected);
        assertEquals(strings(expected), strings(actual));
    }

    @Test
    public void addNullUrl() {
        psk.addIssuerBank(makeBody("000005", "", null));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addNullBin() {
        psk.addIssuerBank(makeBody(null, "", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addIncorrectBin0() {
        psk.addIssuerBank(makeBody("00000", "", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addIncorrectBin1() {
        psk.addIssuerBank(makeBody("0000005", "", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addIncorrectBin2() {
        psk.addIssuerBank(makeBody("kek", "", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addIncorrectBin3() {
        psk.addIssuerBank(makeBody("kekkek", "", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void addExisting() {
        psk.addIssuerBank(makeBody("000002", "", "url2"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void deleteAll() {
        psk.deleteAll();
        assertEquals(0, psk.getAllBanks().size());
    }

    @Test
    public void editOne() {
        String newBin = "000005";
        String oldBin = "000000";
        String newUrl = "url5";
        expected.remove(0);
        expected.add(new IssuerBank(newBin, newUrl));

        psk.editIssuerBank(makeBody(newBin, oldBin, newUrl));

        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editNullUrl() {
        psk.editIssuerBank(makeBody("000005", "000001", null));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editNullBin1() {
        psk.editIssuerBank(makeBody(null, "000001", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editNullBin2() {
        psk.editIssuerBank(makeBody("000005", "", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editIncorrectBin0() {
        psk.editIssuerBank(makeBody("00000", "000001", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editIncorrectBin1() {
        psk.editIssuerBank(makeBody("000005", "00001", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editIncorrectBin21() {
        psk.editIssuerBank(makeBody("kek", "000001", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editIncorrectBin22() {
        psk.editIssuerBank(makeBody("000005", "Kek", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editIncorrectBin3() {
        psk.editIssuerBank(makeBody("kekkek", "000001", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editExisting() {
        psk.editIssuerBank(makeBody("000001", "000000", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editUpdateExisting() {
        psk.editIssuerBank(makeBody("000000", "000000", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    @Test
    public void editNotExisting() {
        psk.editIssuerBank(makeBody("000001", "000010", "url5"));
        assertEquals(strings(expected), strings(psk.getAllBanks()));
    }

    private Map<String, String> makeBody(String newBin, String oldBin, String newUrl) {
        Map<String, String> body = new HashMap<>();
        body.put("newBin", newBin);
        if (oldBin.length() > 0) {
            body.put("delBin", oldBin);
        }
        body.put("newUrl", newUrl);
        return body;
    }

    private List<String> strings(List<IssuerBank> banks) {
        return banks
                .stream()
                .map(IssuerBank::toCompactString)
                .sorted()
                .collect(Collectors.toList());
    }

}