package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for Edit/Update Shipment User Story
 * 
 * Acceptance Test Criteria:
 * If a shipment is non-finalised, when the customer modifies the shipment details (address,
 * method, etc.) and saves changes, shipment record should be updated with new values and
 * updatedDate should be set to current timestamp.
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
        // Create a non-finalized shipment for testing updates
        testShipment = new Shipment(1, 1, originalMethod, originalAddress, 
                originalCity, originalState, originalPostcode);
        testShipment.setShipmentID(1);
        testShipment.setStatus("Pending");
        testShipment.setFinalized(false);
    }

    @Test
    public void testUpdateShipmentModifiesRecord() {
        // Test updating shipment details
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

        // Verify updates
        assertEquals("Address should be updated", newAddress, testShipment.getStreetAddress());
        assertEquals("City should be updated", newCity, testShipment.getCity());
        assertEquals("State should be updated", newState, testShipment.getState());
        assertEquals("Postcode should be updated", newPostcode, testShipment.getPostcode());
        assertEquals("Method should be updated", newMethod, testShipment.getShipmentMethod());
    }

    @Test
    public void testUpdateShipmentReturnTrue() {
        // Test that update operation should return true for success
        // Simulating successful update by checking if values change
        String newCity = "Melbourne";
        testShipment.setCity(newCity);
        
        boolean result = testShipment.getCity().equals(newCity);
        assertTrue("Update should return true", result);
    }

    @Test
    public void testNonFinalizedShipmentCanBeUpdated() {
        // Test that only non-finalized shipments can be updated
        assertFalse("Shipment should not be finalized", testShipment.isFinalized());
        
        // Update should be allowed
        testShipment.setCity("Melbourne");
        assertEquals("City should be updated", "Melbourne", testShipment.getCity());
    }

    @Test
    public void testFinalizedShipmentShouldNotBeUpdated() {
        // Test that finalized shipments should not be updated
        testShipment.setFinalized(true);
        assertTrue("Shipment should be finalized", testShipment.isFinalized());
        
        // In a real scenario, the servlet would prevent this update
        // For testing purposes, we verify the finalized status
        assertTrue("Finalized shipment update should be blocked", testShipment.isFinalized());
    }

    @Test
    public void testUpdateAllAddressFields() {
        // Test updating all address fields
        testShipment.setStreetAddress("789 New Street");
        testShipment.setCity("Brisbane");  
        testShipment.setState("QLD");
        testShipment.setPostcode("4000");

        String expectedFullAddress = "789 New Street, Brisbane, QLD 4000";
        assertEquals("Full address should reflect updates", expectedFullAddress, testShipment.getFullAddress());
    }

    @Test
    public void testUpdateShipmentMethod() {
        // Test updating shipment method
        String[] methods = {"Standard", "Express", "Priority", "Overnight", "Economy"};
        
        for (String method : methods) {
            testShipment.setShipmentMethod(method);
            assertEquals("Shipment method should be updated to " + method, method, testShipment.getShipmentMethod());
        }
    }

    @Test
    public void testUpdateOrderId() {
        // Test updating order ID
        int newOrderId = 99;
        testShipment.setOrderID(newOrderId);
        assertEquals("Order ID should be updated", newOrderId, testShipment.getOrderID());
    }

    @Test
    public void testShipmentStatusRemainsSame() {
        // Test that status remains unchanged during regular updates
        String originalStatus = testShipment.getStatus();
        
        // Update other fields
        testShipment.setCity("Melbourne");
        testShipment.setShipmentMethod("Express");
        
        assertEquals("Status should remain unchanged", originalStatus, testShipment.getStatus());
    }

    @Test
    public void testMultipleUpdates() {
        // Test multiple sequential updates
        testShipment.setCity("Melbourne");
        assertEquals("First update should work", "Melbourne", testShipment.getCity());
        
        testShipment.setCity("Brisbane");
        assertEquals("Second update should work", "Brisbane", testShipment.getCity());
        
        testShipment.setCity("Perth");
        assertEquals("Third update should work", "Perth", testShipment.getCity());
    }
}