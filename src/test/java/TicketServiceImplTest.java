import com.example.trainticket.exception.UserAlreadyBookedException;
import com.example.trainticket.exception.UserNotFoundException;
import com.example.trainticket.model.Receipt;
import com.example.trainticket.model.Seat;
import com.example.trainticket.model.TicketRequest;
import com.example.trainticket.model.User;
import com.example.trainticket.repository.ITicketRepository;
import com.example.trainticket.service.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Mock
    private ITicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;
    @Value("${train.from}")
    private String FROM_LOCATION;
    @Value("${train.to}")
    private String TO_LOCATION;
    @Value("${train.price}")
    private double TICKET_PRICE;
    TicketRequest request = new TicketRequest("john", "valero", "johnvalero@gmail.com");
    User user=new User("john", "valero", "johnvalero@gmail.com");
    Seat seat=new Seat("A", 1);
    Receipt receipt = new Receipt(FROM_LOCATION, TO_LOCATION, user, TICKET_PRICE, seat);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(ticketService, "FROM_LOCATION", "London");
        ReflectionTestUtils.setField(ticketService, "TO_LOCATION", "France");
        ReflectionTestUtils.setField(ticketService, "TICKET_PRICE", 20.0);
    }

    @Test
    void purchaseTicket_success() {
        when(ticketRepository.save(any())).thenReturn(receipt);
        Receipt receipt = ticketService.purchaseTicket(request);
        assertEquals("john", receipt.getUser().getFirstName());
        assertEquals(FROM_LOCATION, receipt.getFrom());
        assertEquals(TO_LOCATION, receipt.getTo());
    }

    @Test
    void purchaseTicket_userAlreadyBooked() {
        when(ticketRepository.findByEmail("johnvalero@gmail.com")).thenReturn(Optional.of(new Receipt()));

        assertThrows(UserAlreadyBookedException.class, () -> ticketService.purchaseTicket(request));
    }

    @Test
    void deleteUser_success() {
        when(ticketRepository.findByEmail("johnvalero@gmail.com")).thenReturn(Optional.of(receipt));
        assertDoesNotThrow(() -> ticketService.removeUser("johnvalero@gmail.com"));
        verify(ticketRepository, times(1)).deleteByEmail("johnvalero@gmail.com");
    }

    @Test
    void deleteUser_userNotFound() {
        when(ticketRepository.findByEmail("john")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> ticketService.removeUser(user.getEmail()));
    }

    @Test
    void testGetReceipt_Success() {
        when(ticketRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(receipt));
        Receipt receipt = ticketService.getReceipt(user.getEmail()).get();
        assertEquals("john", receipt.getUser().getFirstName());
        assertEquals(1, receipt.getSeat().getSeatNumber());
        assertEquals("A", receipt.getSeat().getSection());
    }

    @Test
    void testGetReceipt_UserNotFound() {
        when(ticketRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> ticketService.getReceipt(user.getEmail()));
    }

    @Test
    void testListUsersBySection() {
        List<Receipt> users = Arrays.asList(
                receipt
        );
        when(ticketRepository.findBySection("A")).thenReturn(users);

        List<Receipt> result = ticketService.getUsersBySection("A");

        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(u -> u.getSeat().getSection().equals("A")));
    }


   @Test
    void testModifySeat_Success() {
        when(ticketRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(receipt));

        assertDoesNotThrow(() -> ticketService.modifySeat(user.getEmail(), "B", 2));
        assertEquals(2, receipt.getSeat().getSeatNumber());
        assertEquals("B", receipt.getSeat().getSection());
    }

    @Test
    void testModifySeat_UserNotFound() {
        when(ticketRepository.findByEmail("john")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> ticketService.modifySeat(user.getEmail(), "A", 10));
    }
}
