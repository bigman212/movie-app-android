package ru.redmadrobot.movie_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redmadrobot.movie_list.data.entity.Movie
import ru.redmadrobot.movie_list.databinding.ItemMovieAsListBinding

class MoviesListAdapter(private val movies: MutableList<Movie> = mutableListOf()) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemMovieAsListBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(itemPersonBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    fun replaceAllItems(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun updateMovieRuntime(movieIdToUpdate: Int, movieRuntime: Int) {
        movies.find { it.id == movieIdToUpdate }?.let { movieToUpdate ->
            val indexOf = movies.indexOf(movieToUpdate)
            movies[indexOf] = movieToUpdate.copy(runtime = movieRuntime)
            notifyItemChanged(indexOf)
        }
    }

    override fun getItemCount(): Int = movies.size
}
