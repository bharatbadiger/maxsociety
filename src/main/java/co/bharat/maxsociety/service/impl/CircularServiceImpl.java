package co.bharat.maxsociety.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.bharat.maxsociety.entity.Circulars;
import co.bharat.maxsociety.repository.CircularsRepository;
import co.bharat.maxsociety.service.CircularService;

@Service
public class CircularServiceImpl implements CircularService {

	@Autowired
    private CircularsRepository circularsRepository;


    public Circulars createCircular(Circulars circular) {
        circular.setCircularNo(String.valueOf(circularsRepository.countByCircularType(circular.getCircularType()) + 1L));
        return circularsRepository.save(circular);
    }
}
