export function createHistoryPayloadForStudent(studentId: number, advantageId: number, coins: number, type: string) {
	return {
		coins,
		type,
		studentId,
		advantageId,
		date: new Date(),
	};
}

export function createGenericHistoryPayloadForStudent(studentId: number, coins: number, type: string) {
	return {
		coins,
		type,
		studentId,
		date: new Date(),
	};
}
