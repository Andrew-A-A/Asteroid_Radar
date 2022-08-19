package com.udacity.asteroidradar.main
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

// RecyclerView Adapter for setting up data binding on the items in list.
class AsteroidsAdapter(val onClickListener: OnClickListener):
    ListAdapter<Asteroid,AsteroidsAdapter.AsteroidsViewHolder>(DiffCallback){

    //ViewHolder for Asteroid items  (All work is done by data binding)
    class AsteroidsViewHolder(private val viewDataBinding: AsteroidItemBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(asteroid: Asteroid){
            viewDataBinding.asteroid=asteroid

            viewDataBinding.executePendingBindings()
        }
    }


    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Asteroid]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }



    //Called when RecyclerView needs a new viewHolder if a given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {

        return AsteroidsViewHolder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    // Called by Recyclerview to display the data at specific position
    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
       val asteroid =getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }


    //Click listener for Asteroids.
    class OnClickListener(val clickListener: (asteroid: Asteroid)->Unit){
        //Called when a asteroid is clicked
        fun onClick(asteroid: Asteroid)=clickListener(asteroid)
    }


}


