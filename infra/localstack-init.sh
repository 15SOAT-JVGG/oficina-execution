#!/bin/sh
set -eu
awslocal dynamodb describe-table --table-name oficina-execution-stock-homolog >/dev/null 2>&1 || awslocal dynamodb create-table --table-name oficina-execution-stock-homolog --attribute-definitions AttributeName=partId,AttributeType=S --key-schema AttributeName=partId,KeyType=HASH --billing-mode PAY_PER_REQUEST >/dev/null
