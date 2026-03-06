package com.example.condorapp.data.local

import com.example.condorapp.R
import com.example.condorapp.data.Notification

/** Repositorio local con datos simulados de notificaciones. */
object NotificationRepository {
    fun getNotifications(): List<Notification> =
            listOf(
                    Notification(
                            1,
                            "Maria Valen",
                            "Ahora te sigue",
                            "hace 2 días.",
                            R.drawable.avatar
                    ),
                    Notification(
                            2,
                            "Juan Perez",
                            "Le gustó tu foto",
                            "hace 3 días.",
                            R.drawable.avatar
                    ),
                    Notification(
                            3,
                            "Sofia Castro",
                            "Comentó tu post",
                            "hace 4 días.",
                            R.drawable.avatar
                    ),
                    Notification(
                            4,
                            "Mateo Ruiz",
                            "Ahora te sigue",
                            "hace 5 días.",
                            R.drawable.avatar
                    )
            )
}
