.PHONY: build up

build:
    docker-compose build

up: build
    docker-compose up
