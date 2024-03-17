package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.OwnerRepository;
import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.dto.OwnerDTO;
import ca.mcgill.ecse321.rest.models.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;


    public List<OwnerDTO> findAll() {
        List<OwnerDTO> ownerDTOS = new ArrayList<>() ;
        for (Owner owner : ownerRepository.findAll()) {
            ownerDTOS.add(new OwnerDTO(owner));
        }
        return ownerDTOS;
    }

    public OwnerDTO save(OwnerDTO ownerDTO) {
        Owner owner = convertToEntity(ownerDTO);
        Owner savedOwner = ownerRepository.save(owner);
        return convertToDto(savedOwner);
    }

    private OwnerDTO convertToDto(Owner owner) {
        return new OwnerDTO(owner);
    }

    private Owner convertToEntity(OwnerDTO ownerDTO) {
        Owner owner = new Owner();
        owner.setName(ownerDTO.getName());
        owner.setPhoneNumber(ownerDTO.getPhoneNumber());
        owner.setEmail(ownerDTO.getEmail());
        owner.setSportCenter(sportCenterRepository.findSportCenterById(ownerDTO.getSportCenterId()));
        return owner;
    }
}
