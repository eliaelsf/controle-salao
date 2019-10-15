package com.fabiorHair.controlesalao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Arquivo {
	
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
