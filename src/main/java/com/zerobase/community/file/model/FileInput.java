package com.zerobase.community.file.model;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileInput {
	@NotBlank(message = "FILE_ID_IS_MANDATORY")
	private Long fileId;
}
