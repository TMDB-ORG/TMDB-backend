use actix_web::{web, App, HttpResponse, HttpServer, HttpRequest};
use serde::{Deserialize, Serialize};
use jsonwebtoken::{decode, DecodingKey, Validation};
use chrono::Utc;

#[derive(Debug, Serialize, Deserialize)]
struct Claims {
    sub: String,
    exp: usize,
}

const SECRET_KEY: &str = "minha_chave_nao_segura_que_prometo_mudar_quando_der";

async fn verificar_token(req: HttpRequest) -> HttpResponse {
    let token_header = match req.headers().get("Authorization") {
        Some(val) => val.to_str().unwrap_or("").to_string(),
        None => return HttpResponse::BadRequest().body("Falta Authorization header"),
    };

    let token = token_header.strip_prefix("Bearer ").unwrap_or("");

    match decode::<Claims>(
        token,
        &DecodingKey::from_secret(SECRET_KEY.as_ref()),
        &Validation::default(),
    ) {
        Ok(token_data) => {
            let claims = token_data.claims;

            let agora = Utc::now().timestamp() as usize;
            if claims.exp < agora {
                return HttpResponse::Unauthorized().body("Token expirado");
            }

            HttpResponse::Ok().body(format!("Token válido para o usuário: {}", claims.sub))
        }
        Err(e) => HttpResponse::Unauthorized().body(format!("Token inválido: {}", e)),
    }
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    println!("🚀 Servidor de verificação em http://localhost:5050");

    HttpServer::new(|| {
        App::new()
            .route("/verificar-token", web::get().to(verificar_token))
    })
    .bind(("127.0.0.1", 5050))?
    .run()
    .await
}
