package ncshack.samba.mizan.domain.usecase

class ValidationException(val errors: Map<String, String>) : Exception("Validation failed")
