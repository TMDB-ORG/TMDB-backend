use actix_web::{web, App, HttpRequest, HttpResponse, HttpServer};
use serde::{Deserialize, Serialize};
use jsonwebtoken::{encode, decode, Header, EncodingKey, DecodingKey, Validation};
use chrono::{Utc, Duration};

#[derive(Debug, Serialize, Deserialize)]
struct Claims {
    sub: String,
    exp: usize,
}

const SECRET_KEY: &str = "minha_chave_nao_segura_que_prometo_mudar_quando_der";


async fn gerar_token(info: web::Query<std::collections::HashMap<String, String>>) -> HttpResponse {
    let user_id = match info.get("userId") {
        Some(id) if !id.is_empty() => id.clone(),
        _ => return HttpResponse::BadRequest().body("Falta ou é inválido o parâmetro ?userId"),
    };

    let exp = Utc::now()
        .checked_add_signed(Duration::hours(1))
        .expect("valid timestamp")
        .timestamp() as usize;

    let claims = Claims {
        sub: user_id,
        exp,
    };

    match encode(&Header::default(), &claims, &EncodingKey::from_secret(SECRET_KEY.as_ref())) {
        Ok(token) => HttpResponse::Ok().body(token),
        Err(_) => HttpResponse::InternalServerError().body("Erro ao gerar token"),
    }
}


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
    println!("🚀 Servidor rodando em http://127.0.0.1:8081 (Gerar Token)");
    println!("🚀 Servidor de verificação em http://127.0.0.1:5050 (Verificar Token)");

    HttpServer::new(|| {
        App::new()
            .route("/gerar-token", web::get().to(gerar_token))  
            .route("/verificar-token", web::get().to(verificar_token))  
    })
    .bind(("127.0.0.1", 8081))?  
    .run()
    .await
}
