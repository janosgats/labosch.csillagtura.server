package com.laboschqpa.server.api.service;

import com.laboschqpa.server.entity.RegistrationRequest;
import com.laboschqpa.server.enums.RegistrationRequestPhase;
import com.laboschqpa.server.exceptions.joinflow.RegistrationJoinFlowException;
import com.laboschqpa.server.model.sessiondto.JoinFlowSessionDto;
import com.laboschqpa.server.repo.RegistrationRequestRepository;
import com.laboschqpa.server.repo.UserEmailAddressRepository;
import com.laboschqpa.server.service.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisterByEmailService {
    private final RegistrationRequestRepository registrationRequestRepository;
    private final UserEmailAddressRepository userEmailAddressRepository;
    private final EmailSenderService emailSenderService;

    public void onSubmitEmailToRegister(String emailToRegister) {
        if (userEmailAddressRepository.findByEmail(emailToRegister).isPresent()) {
            throw new RegistrationJoinFlowException("E-mail address is already in the system!");
        }

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(emailToRegister);
        registrationRequest.setKey(RandomStringUtils.random(100, 0, 0, true, true, null, new SecureRandom()));
        registrationRequest.setPhase(RegistrationRequestPhase.EMAIL_SUBMITTED);
        registrationRequestRepository.save(registrationRequest);

        emailSenderService.sendRegistrationRequestMail(emailToRegister, registrationRequest.getId(), registrationRequest.getKey());
    }

    public void onVisitingPageFromEmailLink(Long registrationRequestId, String registrationRequestKey) {
        RegistrationRequest registrationRequest = getValidRegistrationRequest(registrationRequestId, registrationRequestKey);

        JoinFlowSessionDto joinFlowSessionDto = new JoinFlowSessionDto();
        joinFlowSessionDto.setRegistrationRequestId(registrationRequest.getId());
        joinFlowSessionDto.writeToCurrentSession();

        registrationRequest.setPhase(RegistrationRequestPhase.EMAIL_VERIFIED);
        registrationRequestRepository.save(registrationRequest);
    }

    private RegistrationRequest getValidRegistrationRequest(Long registrationRequestId, String registrationRequestKey) {
        Optional<RegistrationRequest> registrationRequestOptional = registrationRequestRepository.findById(registrationRequestId);
        if (registrationRequestOptional.isEmpty()) {
            throw new RegistrationJoinFlowException("Registration request cannot be found!");
        }
        RegistrationRequest registrationRequest = registrationRequestOptional.get();

        if (!registrationRequest.getPhase().equals(RegistrationRequestPhase.EMAIL_SUBMITTED) && !registrationRequest.getPhase().equals(RegistrationRequestPhase.EMAIL_VERIFIED)) {
            throw new RegistrationJoinFlowException("Registration request is not in appropriate state for verifying the e-mail address. " +
                    "Please submit a new registration request if you don't have an account yet!");
        }

        if (registrationRequest.getKey().equals(registrationRequestKey)) {
            return registrationRequest;
        } else {
            throw new RegistrationJoinFlowException("Registration request key is not matching!");
        }
    }
}
