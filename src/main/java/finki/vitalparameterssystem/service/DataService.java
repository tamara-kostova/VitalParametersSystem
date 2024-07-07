package finki.vitalparameterssystem.service;

import java.io.IOException;
import java.util.List;

public interface DataService {
    String sendTemperatureToPython(List<Double> temperatureList) throws IOException;
    String sendSaturationToPython(List<Double> saturationList) throws IOException;
    String sendRespirationRateToPython(List<Integer> respirationRateList) throws IOException;
    String sendPulseToPython(List<Integer> pulseList) throws IOException;
    String sendSystolicToPython(List<Integer> systolicList) throws IOException;
}
