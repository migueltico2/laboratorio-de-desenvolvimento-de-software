import nodemailer from 'nodemailer';

let transporter = nodemailer.createTransport({
	service: 'gmail',
	auth: {
		user: 'medsyncmailer@gmail.com',
		pass: 'gqvg iguj spbj lltk',
	},
});

export async function sendMailOnCoinSend(to, moedas, reason) {
	try {
		// TODO: Alterar valores estáticos para valores de env var
		const mailOptions = {
			from: '"MedSync" <medsyncmailer@gmail.com>',
			to: to,
			subject: 'Recebimento de moedas',
			html: `<div style="border: 1px solid black; padding: 20px; width: 500px; max-width: 100%; margin: 0 auto; text-align: center;">
  <h1>Recebimento de moedas</h1>
  <p>Você recebeu ${moedas} por ${reason}</p>
</div>`,
		};

		return await transporter.sendMail(mailOptions);
	} catch (error) {
		console.log(error);
	}
}

export async function sendMailOnAdvantageRedeem(to, advantage) {
	const code = Math.random().toString(36).substring(2, 8).toUpperCase();

	try {
		const mailOptions = {
			from: '"MedSync" <medsyncmailer@gmail.com>',
			to: to,
			subject: 'Recebimento de benefício',
			html: `<div style="border: 1px solid black; padding: 20px; width: 500px; max-width: 100%; margin: 0 auto; text-align: center;">
  <h1>Recebimento de benefício</h1>
  <p>Você recebeu um cupom para troca de vantagem</p>
  <p>O código para resgatar a vantagem é: ${code}</p>
</div>`,
		};

		return await transporter.sendMail(mailOptions);
	} catch (error) {
		console.log(error);
	}
}
