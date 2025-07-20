# Rinha de Backend 2025 - v1 (Java Spring Boot)

**Autor:** AndersonDev  
**Desafio:** [Rinha de Backend 2025](https://github.com/zanfranceschi/rinha-de-backend-2025)  
**Status:** Primeira versão (v1) — Java + Spring Boot

---

## Sobre o Projeto

Este repositório implementa a primeira versão do desafio "Rinha de Backend 2025". 
O objetivo principal foi entregar uma solução performática, 
sem abrir mão das boas práticas de arquitetura, organização e código limpo — ou seja, sem gambiarras.

O projeto está preparado para evoluir em duas próximas versões:
- **v2:** Java com Quarkus, buscando o máximo desempenho possível.
- **v3:** Reimplementação em Go (Golang).

---

## Arquitetura e Organização

- **Java 24 + Spring Boot:** Estrutura modular, separando responsabilidades em camadas (controller, domínio, infraestrutura).
- **Persistência:** PostgreSQL para armazenamento dos pagamentos.
- **Mensageria:** Integração com AWS SQS (simulada via LocalStack).
- **Cache:** Redis para otimizações e controle de processadores.
- **Testes de carga:** Scripts prontos usando k6.

### Principais Pacotes

- `controller`: Exposição dos endpoints REST.
- `domain.model`: Entidades, DTOs e enums do domínio.
- `domain.services`: Interfaces e implementações das regras de negócio.
- `infra`: Integrações (AWS, banco, cache, clientes HTTP, utilitários).

---

## Endpoints

- `POST /payments`: Recebe um pagamento (correlationId, amount).
- `GET /payments-summary?from&to`: Retorna um resumo dos pagamentos processados, agrupados por processador (default/fallback).

### Exemplo de Payload

```json
POST /payments
{
  "correlationId": "uuid",
  "amount": 100.50
}
```

### Resposta de Resumo

```json
GET /payments-summary
{
  "default": { "totalRequest": 10, "totalAmount": 1000.00 },
  "fallback": { "totalRequest": 2, "totalAmount": 200.00 }
}
```

---

## Como rodar o projeto

### 1. Suba as dependências

```sh
docker-compose up -d
cd payment-processor
docker-compose up -d
```

### 2. Execute a aplicação

Você pode rodar via IDE ou usando Maven:

```sh
./mvnw spring-boot:run
```

### 3. Teste a API

Utilize os scripts de teste localizados em `rinha-test`:

```sh
cd rinha-test
k6 run rinha.js
```

Veja o [README de testes](rinha-test/README.md) para mais detalhes.

---

## Roadmap das Próximas Versões

- **v2:** Java + Quarkus, foco em desempenho extremo.
- **v3:** Go (Golang), para explorar o máximo de performance e concorrência.

---

## Processamento Assíncrono e Inteligência de Escolha do Processor

O fluxo de processamento de pagamentos é totalmente **assíncrono**:
- Ao receber um pagamento via `POST /payments`, ele é persistido e enviado para uma fila (SQS/LocalStack).
- Um consumidor dedicado processa os pagamentos em background, garantindo alta performance e desacoplamento entre recebimento e processamento.
- **Os consumidores da fila rodam em threads virtuais**, aproveitando o máximo de concorrência e escalabilidade do Java moderno.

### Inteligência de Seleção do Processor

O sistema implementa uma lógica inteligente para escolher entre dois processadores de pagamento externos:
- **Processor Default** (taxa menor, sempre prioridade)
- **Processor Fallback** (taxa maior, usado apenas em caso de falha do Default)

#### Health-Check Dinâmico
- A cada **5 segundos**, o sistema realiza health-checks nos dois processors.
- O health-check retorna, além do status, o campo `minResponseTime` de cada processor.
- O resultado do health-check determina qual processor está disponível e saudável para uso.
- O sistema **só utiliza o Fallback se a diferença de tempo de resposta (`minResponseTime`) for aceitável** em relação ao Default, evitando custos desnecessários e priorizando sempre o Default.
- Caso ambos estejam fora do ar, o pagamento é rejeitado temporariamente ("servers unavailable").
- O sistema respeita rigorosamente o intervalo mínimo de 5 segundos entre health-checks, conforme as regras do desafio.

#### Estratégia de Otimização de Taxas
- O objetivo é processar o **máximo de pagamentos possível**, sempre priorizando o processor com a **menor taxa** (Default).
- Se o Default estiver indisponível ou com tempo de resposta muito alto, o Fallback é utilizado automaticamente, desde que a diferença de performance seja aceitável.
- Quando o Default volta a ficar saudável e rápido, ele volta a ser priorizado imediatamente.

Essa abordagem garante:
- **Alta disponibilidade**: tolerância a falhas dos processors.
- **Baixa latência**: processamento assíncrono e desacoplado.
- **Otimização de custos**: sempre buscando a menor taxa possível, sem sacrificar a performance.

---

**AndersonDev** 