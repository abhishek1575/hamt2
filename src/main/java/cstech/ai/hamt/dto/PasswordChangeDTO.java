package cstech.ai.hamt.dto;

import lombok.Data;

@Data
public class PasswordChangeDTO {


    private Long id;

    private String oldPassword;

    private String newPassword;


}