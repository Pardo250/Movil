package com.example.condorapp.data.local

import com.example.condorapp.data.Notification

/** Repositorio local con datos simulados de notificaciones. */
object NotificationRepository {
        fun getNotifications(): List<Notification> =
                listOf(
                        /*Notification(
                                1,
                                "Maria Valen",
                                "Ahora te sigue",
                                "hace 2 días.",
                                "https://i.pravatar.cc/150?img=1"
                        ),
                        Notification(
                                2,
                                "Juan Perez",
                                "Le gustó tu foto",
                                "hace 3 días.",
                                "https://i.pravatar.cc/150?img=2"
                        ),
                        Notification(
                                3,
                                "Sofia Castro",
                                "Comentó tu post",
                                "hace 4 días.",
                                "https://i.pravatar.cc/150?img=3"
                        ),
                        Notification(
                                4,
                                "Mateo Ruiz",
                                "Ahora te sigue",
                                "hace 5 días.",
                                "https://i.pravatar.cc/150?img=4"
                        )*/
                )
}
