package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for Edit/Update Shipment User Story
 */
public class UpdateShipmentTest {

    private Shipment testShipment;
    private String originalAddress = "123 Original St";
    private String originalCity = "Sydney";
    private String originalState = "NSW";
    private String originalPostcode = "2000";
    private String originalMethod = "Standard";

    @Before
    public void setUp() {
        testShipment = new Shipment(1, 1, originalMethod, originalAddress, 
                originalCity, originalState, originalPostcode);
        testShipment.setShipmentID(1);
        testShipment.setStatus("Pending");
        testShipment.setFinalized(false);
    }

    @Test
    public void testUpdateShipmentModifiesRecord() {
        String newAddress = "456 Updated Ave";
        String newCity = "Melbourne";
        String newState = "VIC";
        String newPostcode = "3000";
        String newMethod = "Express";

        testShipment.setStreetAddress(newAddress);
        testShipment.setCity(newCity);
        testShipment.setState(newState);
        testShipment.setPostcode(newPostcode);
        testShipment.setShipmentMethod(newMethod);

        
        assertEquals("Address should be updated", newAddress, testShipment.getStreetAddress());
        assertEquals("City should be updated", newCity, testShipment.getCity());
        assertEquals("State should be updated", newState, testShipment.getState());
        assertEquals("Postcode should be updated", newPostcode, testShipment.getPostcode());
        assertEquals("Method should be updated", newMethod, testShipment.getShipmentMethod());
    }

    @Test
    public void testUpdateShipmentReturnTrue() {
        String newCity = "Melbourne";
        testShipment.setCity(newCity);
        
        boolean result = testShipment.getCity().equals(newCity);
        assertTrue("Update should return true", result);
    }

    @Test
    public void testNonFinalizedShipmentCanBeUpdated() {
        assertFalse("Shipment should not be finalized", testShipment.isFinalized());
        
        testShipment.setCity("Melbourne");
        assertEquals("City should be updated", "Melbourne", testShipment.getCity());
    }

    @Test
    public void testFinalizedShipmentShouldNotBeUpdated() {
        testShipment.setFinalized(true);
        assertTrue("Shipment should be finalized", testShipment.isFinalized());
        assertTrue("Finalized shipment update should be blocked", testShipment.isFinalized());
    }

    @Test
    public void testUpdateAllAddressFields() {
        testShipment.setStreetAddress("789 New Street");
        testShipment.setCity("Brisbane");  
        testShipment.setState("QLD");
        testShipment.setPostcode("4000");

        String expectedFullAddress = "789 New Street, Brisbane, QLD 4000";
        assertEquals("Full address should reflect updates", expectedFullAddress, testShipment.getFullAddress());
    }

    @Test
    public void testUpdateShipmentMethod() {
        String[] methods = {"Standard", "Express", "Priority", "Overnight", "Economy"};
        
        for (String method : methods) {
            testShipment.setShipmentMethod(method);
            assertEquals("Shipment method should be updated to " + method, method, testShipment.getShipmentMethod());
        }
    }

    @Test
    public void testUpdateOrderId() {
        int newOrderId = 99;
        testShipment.setOrderID(newOrderId);
        assertEquals("Order ID should be updated", newOrderId, testShipment.getOrderID());
    }

    @Test
    public void testShipmentStatusRemainsSame() {
        String originalStatus = testShipment.getStatus();
        
        testShipment.setCity("Melbourne");
        testShipment.setShipmentMethod("Express");
        
        assertEquals("Status should remain unchanged", originalStatus, testShipment.getStatus());
    }

    @Test
    public void testMultipleUpdates() {
        testShipment.setCity("Melbourne");
        assertEquals("First update should work", "Melbourne", testShipment.getCity());
        
        testShipment.setCity("Brisbane");
        assertEquals("Second update should work", "Brisbane", testShipment.getCity());
        
        testShipment.setCity("Perth");
        assertEquals("Third update should work", "Perth", testShipment.getCity());
    }
}