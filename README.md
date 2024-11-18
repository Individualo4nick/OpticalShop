Интернет-магазин на микросервисной архитектуре

Этот проект создан как тренировочный, но он реализует все базовые функции, необходимые для полноценного интернет-магазина.

Основные особенности:

	•	Микросервисная архитектура: построена с использованием Spring Cloud.
	•	Service Discovery: реализован через Eureka Server для управления взаимодействием между сервисами.
	•	Раздельные базы данных: каждый микросервис использует собственную базу данных для повышения изоляции и гибкости.
	•	Авторизация и безопасность: применяется JWT-токен для обеспечения безопасности.
	•	Развёртывание: проект развёрнут с использованием Docker Compose.
	•	Метрики и мониторинг:
	•	Настроен сбор метрик через Prometheus.
	•	Визуализация данных организована в Grafana.

Используемый стек технологий:

	•	Spring Boot
	•	Spring Cloud
	•	Docker
	•	PostgreSQL
	•	Hibernate
	•	Redis
	•	Prometheus
	•	Grafana
