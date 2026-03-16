# Spring E-commerce API

![Java](https://img.shields.io/badge/Java-25+-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue?style=flat-square&logo=postgresql)
![Status](https://img.shields.io/badge/status-in_development-yellow?style=flat-square)

---

API REST completa de e-commerce construída com Spring Boot. Sistema real com carrinho de compras, processamento de pedidos, controle de estoque e busca de produtos.

---

## O Problema que Resolvi

Queria aprender Spring Boot construindo algo além de CRUD básico. Então criei um e-commerce de verdade, com os mesmos desafios de um sistema real:

- **Carrinho persistente** - Items salvos no banco, não em sessão
- **Snapshot de preço** - Quando você cria um pedido, guarda o preço daquele momento (produto pode mudar depois)
- **Controle de estoque** - Verifica disponibilidade antes de adicionar ao carrinho
- **Soft delete** - Produtos são desativados, não deletados (histórico de pedidos continua funcionando)
- **Evitar N+1 queries** - Relacionamentos configurados pra não explodir o banco

---

## Stack Técnica

- **Backend:** Java 25, Spring Boot 4.x  
- **Persistência:** Spring Data JPA, Hibernate, PostgreSQL  
- **Arquitetura:** Camadas (Controller -> Service -> Repository)  
- **Padrões:** DTOs para Request/Response, Soft Delete, Timestamp Automático

---

## Modelagem do Banco

```
User (1) -> (1) Address
  |
   ->(N) CartItem -> (1) Product
  |
   -> (N) Order
            |
             -> (N) OrderItem -> (1) Product
```

**Decisões de design:**

- **CartItem e OrderItem separados** - CartItem é temporário, OrderItem é histórico imutável
- **Price nos items** - Guarda o preço no momento da compra (snapshot), não referencia Product.price
- **BigDecimal** - Valores monetários precisam ser exatos (nunca usar Double/Float)
- **Soft delete** - `Product.active = false` em vez de DELETE (mantém integridade dos pedidos antigos)

---

## Features Implementadas

### 1. Gerenciamento de Produtos
- CRUD completo com soft delete
- Busca por palavra-chave (nome, descrição, categoria)
- Controle de estoque automático
- Apenas produtos ativos aparecem na listagem

### 2. Carrinho de Compras
```java
// Se produto já está no carrinho -> atualiza quantidade
// Se é novo -> cria CartItem
// Sempre valida estoque antes de adicionar
```
- Adicionar/remover items
- Atualização automática de subtotal
- Validação de estoque em tempo real
- Carrinho persistido no banco (não em memória)

### 3. Sistema de Pedidos
**Fluxo completo:**
1. Busca itens do carrinho do usuário
2. Calcula total
3. Cria Order com status CONFIRMED
4. Converte CartItems em OrderItems (snapshot de preço)
5. Limpa o carrinho
6. Retorna OrderResponse com todos os detalhes

**Estados do pedido:** PENDING -> CONFIRMED -> SHIPPED -> DELIVERED (ou CANCELLED)

### 4. Gestão de Usuários
- CRUD de usuários
- Endereço em entidade separada (OneToOne)
- Roles: CUSTOMER e ADMIN (preparado pra autorização futura)

---

## Boas Práticas Aplicadas

### Código Limpo
- **DTOs separados** - Request e Response nunca expõem entidades diretamente
- **Métodos privados de mapeamento** - `mapToProductResponse()`, `updateUserFromRequest()`
- **Nomes descritivos** - `clearCart()`, `deleteItemFromCart()`, não abreviações
- **Single Responsibility** - Cada service cuida de uma entidade

### JPA/Hibernate
- `@CreationTimestamp` e `@UpdateTimestamp` - Auditoria automática
- `cascade = CascadeType.ALL` em relacionamentos pai-filho
- `orphanRemoval = true` - Deleta OrderItems quando Order é deletado
- Soft delete com campo `active` (não quebra foreign keys)

### Transações
```java
@Transactional // Garante atomicidade no addToCart
```
- `addToCart()` é transacional - ou salva tudo ou nada
- Evita condições de corrida no estoque

### Validações
- Verificação de estoque antes de adicionar ao carrinho
- Retornos booleanos claros (`true` = sucesso, `false` = falha)
- `Optional<>` nos métodos que podem não encontrar resultados

---

## Desafios Técnicos

### 1. Snapshot de Preço
**Problema:** Se eu guardo só `product_id` no OrderItem, o preço pode mudar depois.  
**Solução:** Copiei `price` e `quantity` pro OrderItem no momento da compra.

### 2. Atualizar vs Criar CartItem
**Problema:** Se usuário adiciona produto que já está no carrinho, duplica?  
**Solução:** Query `findByUserAndProduct()` - se existe, soma quantidade; senão, cria novo.

### 3. Soft Delete de Produtos
**Problema:** Se eu deletar produto, os pedidos antigos quebram.  
**Solução:** Campo `active = false` + repository retorna só `findByActiveTrue()`.

---

## Próximos Passos

**Curto prazo (já planejado):**
- [ ] Autenticação JWT (substituir header `X-User-ID`)
- [ ] Spring Security com roles (ADMIN pode criar produtos, CUSTOMER só compra)
- [ ] Atualizar estoque ao criar pedido (decrementar `stockQuantity`)
- [ ] Paginação nos endpoints de listagem

**Médio prazo (ideias):**
- [ ] Histórico de pedidos por usuário (`/api/orders/my-orders`)
- [ ] Filtros avançados de produtos (preço min/max, categoria)
- [ ] Integração com gateway de pagamento (Stripe/PagSeguro mockado)
- [ ] Testes unitários com JUnit/Mockito

---

**Nota técnica:** O header `X-User-ID` é temporário. No sistema final, o userId virá do token JWT decodificado no filtro de autenticação. Mantive assim pra focar primeiro na lógica de negócio antes de adicionar a camada de segurança.
