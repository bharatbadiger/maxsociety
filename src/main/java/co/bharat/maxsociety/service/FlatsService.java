package co.bharat.maxsociety.service;

import java.util.Optional;

import co.bharat.maxsociety.entity.Flats;

public interface FlatsService {

	   Flats createUser(Flats flats);
	   
	   Optional<Flats> getUser(String id);

	   Flats updateFlats(String flatNo, Flats flats);
}
