use actix_web::{web, App, HttpRequest, HttpResponse, HttpServer};
use serde::{Deserialize, Serialize};
use jsonwebtoken::{encode, Header, EncodingKey};
use chrono::{Utc, Duration};

#[derive(Debug, Serialize, Deserialize)]
struct Claims {
    sub: String,
    exp: usize,
}

async fn gerar_token(req: HttpRequest) -> HttpResponse {
    let user_id = match req.headers().get("X-User-ID") {
        Some(val) => match val.to_str() {
            Ok(s) => s.to_string(),
            Err(_) => return HttpResponse::BadRequest().body("Invalid X-User-ID header"),
        },
        None => return HttpResponse::BadRequest().body("Falta X-User-ID header"),
    };

    if user_id.is_empty() {
        return HttpResponse::BadRequest().body("X-User-ID não pode ser vazio");
    }

    let exp = Utc::now()
        .checked_add_signed(Duration::hours(1))
        .expect("valid timestamp")
        .timestamp() as usize;

    let claims = Claims {
        sub: user_id,
        exp,
    };

    let secret = "minha_chave_nao_segura_que_prometo_mudar_quando_der";

    match encode(&Header::default(), &claims, &EncodingKey::from_secret(secret.as_ref())) {
        Ok(token) => HttpResponse::Ok().body(token),
        Err(_) => HttpResponse::InternalServerError().body("Erro ao gerar token"),
    }
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    println!("Servidor rodando em http://127.0.0.1:8080");

    HttpServer::new(|| {
        App::new()
            .route("/gerar-token", web::get().to(gerar_token))
    })
    .bind(("127.0.0.1", 8080))?
    .run()
    .await
}