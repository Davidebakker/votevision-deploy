package com.election.backendjava.controllers.elections;

import com.election.backendjava.controllers.ElectionController;
import com.election.backendjava.models.election.Party;
import com.election.backendjava.repositories.election.PartyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ElectionControllerTest {

    @Mock
    private PartyRepository partyRepository;

    @InjectMocks
    private ElectionController electionController;

    @Test
    void testGetAllParties() {
        Party party1 = new Party(1L, "Party A", "logoA.png", 10);
        Party party2 = new Party(2L, "Party B", "logoB.png", 15);
        List<Party> mockParties = Arrays.asList(party1, party2);

        when(partyRepository.findAll()).thenReturn(mockParties);

        List<Party> result = electionController.getAllParties();

        assertEquals(2, result.size());
        assertEquals("Party A", result.get(0).getName());
        assertEquals("Party B", result.get(1).getName());
    }
}
