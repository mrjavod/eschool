package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeachersPayloadDetails extends TeachersPayload {

    private String inn;
    private String inps;
    private String passportSeries;
    private String passportNumber;
    private String address;

    private FilesPayload diploma;
    private FilesPayload passport;
    private FilesPayload avatar;
}
