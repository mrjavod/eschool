package com.home.eschool.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayOutputStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportPayload {

    private String fileName;
    private ByteArrayOutputStream content;
}
