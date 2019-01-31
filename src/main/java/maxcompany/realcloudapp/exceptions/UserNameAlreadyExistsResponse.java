package maxcompany.realcloudapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserNameAlreadyExistsResponse {

    private String username;

}
