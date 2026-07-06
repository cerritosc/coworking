package com.cuscatlan.coworking.service;

import com.cuscatlan.coworking.entity.Reservation;

public interface EmailNotificationService {
	
	void sendReservationConfirmation(Reservation reservation);

}
