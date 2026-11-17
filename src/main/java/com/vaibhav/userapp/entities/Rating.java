package com.vaibhav.userapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
	private Long ratingId;
	private Long hotelId;
	private Long userId;
	private String rating;
	private String remark;
}
