package cstech.ai.hamt.dto;

import cstech.ai.hamt.entity.Role;
import lombok.Data;

@Data
public class UserDto {


    private Long id;

    private String name;

    private String email;

    private Role role;


}

