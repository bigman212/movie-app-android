package ru.redmadrobot.movie_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redmadrobot.movie_list.Movie
import ru.redmadrobot.movie_list.databinding.ItemMovieAsListBinding

class MoviesListAdapter(private val movies: MutableList<Movie> = mutableListOf()) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemMovieAsListBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(itemPersonBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(movies[position])

    fun addAll(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size
}
