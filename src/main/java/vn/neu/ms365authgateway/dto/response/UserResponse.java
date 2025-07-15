package vn.neu.ms365authgateway.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;

    String firstName;

    String lastName;

    String fullName;

    String majorClass;

    String major;

    String faculty;

    String school;

    String course;

    String dateOfBirth;

    String sexuality;

    String ethnicGroup;

    String hometown;

    String placeOfBirth;

    String religion;

    String email;

    String idCardNumber;

    String idIssuePlace;

    String idIssueDate;

    String phoneNumber;

}
