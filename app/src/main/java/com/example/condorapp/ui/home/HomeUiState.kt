package com.example.condorapp.ui.home

import com.example.condorapp.data.Post

/** Estado del UI para la pantalla Home. Contiene la lista de posts y el post seleccionado. */
data class HomeUiState(val posts: List<Post> = emptyList(), val selectedPostIndex: Int? = null)
