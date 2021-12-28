package org.komapper.example.model

import org.komapper.example.entity.Account
import org.komapper.example.entity.Profile
import org.komapper.example.entity.SignOn

class AccountAggregate(
    val account: Account,
    val profile: Profile,
    val signOn: SignOn,
)
