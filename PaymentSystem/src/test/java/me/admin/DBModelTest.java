package me.admin;

import org.junit.*;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DBModelTest {
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
    }

    @Test
    public void openDBAddPage() {
        Model model = new ExtendedModelMap();
        psk.openDBAddPage(model);
        assertTrue(model.containsAttribute("bank"));
        assertTrue(model.getAttribute("bank") instanceof IssuerBank);
    }

    @Test
    public void openDBEditPage() {
        Model model = new ExtendedModelMap();
        psk.openDBEditPage(model);
        assertTrue(model.containsAttribute("allBanks"));
        assertTrue(model.getAttribute("allBanks") instanceof List);
        assertTrue(((List) model.getAttribute("allBanks")).get(0) instanceof IssuerBank);
        assertEquals(strings(((List) model.getAttribute("allBanks"))), strings(expected));
    }

    private List<String> strings(List<IssuerBank> banks) {
        return banks
                .stream()
                .map(IssuerBank::toCompactString)
                .sorted()
                .collect(Collectors.toList());
    }
}
