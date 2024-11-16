export interface CreateProfessorDTO {
	name: string;
	email: string;
	password: string;
	CPF: string;
	department: string;
	institutionId: number;
}

export interface UpdateProfessorDTO extends Partial<CreateProfessorDTO> {}
