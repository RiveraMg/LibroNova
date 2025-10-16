-- Script para corregir la columna refund_datetime en la tabla Loans
-- Ejecutar este script en MySQL para permitir valores NULL en refund_datetime

USE LibroNova;

-- Modificar la columna refund_datetime para permitir NULL
ALTER TABLE Loans MODIFY COLUMN refund_datetime DATE NULL;

-- Verificar que el cambio se aplicó correctamente
DESCRIBE Loans;
