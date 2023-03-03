package com.zerobase.community.file.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileId;
	private String originName;
	private String savedName;
	private String savedPath;

	private Long postId;

}
