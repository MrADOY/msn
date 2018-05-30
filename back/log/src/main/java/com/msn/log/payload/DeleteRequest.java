package com.msn.log.payload;

import javax.validation.constraints.*;

public class DeleteRequest {

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
