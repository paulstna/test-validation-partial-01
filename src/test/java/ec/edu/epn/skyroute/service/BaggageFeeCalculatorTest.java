package ec.edu.epn.skyroute.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BaggageFeeCalculatorTest {

    @Mock
    PassengerService passengerService;

    @InjectMocks
    BaggageFeeCalculator baggageFeeCalculator;


    @Test
    @DisplayName(value = "Equipaje estándar 1 maleta, 20 kg, pasajero regular Esperado -> $30.00")
    public void regularPassenger_OneBag_Under23kg_ShouldChargeStandardFee() {
        //Arrange
        int numMaletas = 1;
        double weightMaletas = 20;
        long passagerId = 1L;
        double expectResult = 30;
        when(passengerService.isVip(passagerId)).thenReturn(false);
        //Act
        double fee = baggageFeeCalculator.calculateFee(weightMaletas, numMaletas, passagerId);

        //Assert
        assertEquals(expectResult, fee, 0.00);
    }

    @Test
    @DisplayName(value = "Exceso de peso 1 maleta, 25 kg, pasajero regular Esperado -> $80.00")
    public void regularPassenger_OneBag_Above23kg_ShouldChargeExcessWeightFee() {
        //Arrange
        int numMaletas = 1;
        double weightMaletas = 25;
        long passagerId = 1L;
        double expectResult = 80;
        when(passengerService.isVip(passagerId)).thenReturn(false);
        //Act
        double fee = baggageFeeCalculator.calculateFee(weightMaletas, numMaletas, passagerId);

        //Assert
        assertEquals(expectResult, fee, 0.00);
    }

    @Test
    @DisplayName(value = "Beneficio VIP 1 maleta, 15 kg, pasajero VIP Esperado-> $0.00 ")
    public void vipPassenger_OneBag_Under23kg_ShouldBeFree() {
        //Arrange
        int numMaletas = 1;
        double weightMaletas = 15;
        long passagerId = 1L;
        double expectResult = 0;
        when(passengerService.isVip(passagerId)).thenReturn(true);
        //Act
        double fee = baggageFeeCalculator.calculateFee(weightMaletas, numMaletas, passagerId);

        //Assert
        assertEquals(expectResult, fee, 0.00);
    }

    @Test
    @DisplayName(value = "Caso límite VIP 2 maletas, 15 kg c/u, pasajero VIP Esperado" +
            " -> $30.00 (1ra gratis, 2da cobro normal)")
    public void vipPassenger_TwoBags_Under23kg_ShouldChargeOnlySecondBag() {
        //Arrange
        int numMaletas = 2;
        double weightMaletas = 15;
        long passagerId = 1L;
        double expectResult = 30;
        when(passengerService.isVip(passagerId)).thenReturn(true);
        //Act
        double fee = baggageFeeCalculator.calculateFee(weightMaletas, numMaletas, passagerId);

        //Assert
        assertEquals(expectResult, fee, 0.00);
    }

    @Test
    @DisplayName(value = "Validación de excepción weight = 0 o negativo Esperado " +
            " -> IllegalArgumentException")
    public void calculateFee_WithZeroOrNegativeWeight_ShouldThrowIllegalArgumentException() {
        //Arrange
        int numMaletas = 2;
        double weightMaletas = 0;
        long passagerId = 1L;
        String expectExceptionMesssageResult = "Parámetros de equipaje inválidos";

        //Act and Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> baggageFeeCalculator.calculateFee(weightMaletas, numMaletas, passagerId)
        );
        assertEquals(expectExceptionMesssageResult, ex.getMessage());
    }
}
