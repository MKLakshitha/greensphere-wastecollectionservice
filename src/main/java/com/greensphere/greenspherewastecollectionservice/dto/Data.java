package com.greensphere.greenspherewastecollectionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data{
	@JsonProperty("app_user")
	private AppUser appUser;
	@JsonProperty("user_details")
	private UserDetail userDetails;
}
