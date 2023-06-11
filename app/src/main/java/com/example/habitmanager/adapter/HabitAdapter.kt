package com.example.habitmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.habit.comparator.HabitComparatorByCategory
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.ItemHabitBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.java.KoinJavaComponent.get
import java.util.Collections

class HabitAdapter(
    listener: OnItemClickListener
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {
    private val list: ArrayList<Habit> = ArrayList()
    private val listener: OnItemClickListener
    private val positionDescriptionShowed: ArrayList<Int>
    private val categoryRepository: CategoryRepository = get(CategoryRepository::class.java)
    var selectedPosition = -1
    var deletedPosition = -1

    init {
        this.listener = listener
        positionDescriptionShowed = ArrayList()
    }

    fun deleteHabit(position: Int) {
        deletedPosition = position
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Habit {
        return list[position]
    }

    fun orderByCategory() {
        Collections.sort(list, HabitComparatorByCategory())
        notifyDataSetChanged()
    }

    fun orderByName() {
        list.sort()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = list[position].name
        holder.binding.avatarImageView2.setImageResource(
            categoryRepository.getPicture(
                list[position].categoryId!!
            )

        )
        holder.binding.descriptionCotent.text = list[position].description
        holder.binding.root.isSelected = position == selectedPosition
        if (!positionDescriptionShowed.contains(position)) {
            hideDescription(holder)
        } else {
            showDescription(holder)
        }
    }

    private fun hideDescription(holder: ViewHolder) {
        holder.binding.description.visibility = View.GONE
        holder.binding.descriptionnBtn.setImageResource(R.drawable.ic_expand)
        holder.binding.descriptionnBtn.setOnClickListener { view: View? ->
            holder.binding.description.visibility = View.VISIBLE
            val animation = AlphaAnimation(0f, 1.0f)
            animation.duration = 500
            animation.fillAfter = true
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    // Este método no es necesario para la animación, pero de debe implementar
                }

                override fun onAnimationEnd(animation: Animation) {
                    holder.addPosition()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // Este método no es necesario para la animación, pero de debe implementar
                }
            })
            holder.binding.description.startAnimation(animation)
        }
    }

    private fun showDescription(holder: ViewHolder) {
        holder.binding.description.visibility = View.VISIBLE
        holder.binding.descriptionnBtn.setImageResource(R.drawable.ic_hide)
        holder.binding.descriptionnBtn.setOnClickListener { view: View? ->
            val animation = AlphaAnimation(1.0f, 0f)
            animation.duration = 500
            animation.fillAfter = true
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    // Este método no es necesario para la animación, pero de debe implementar
                }

                override fun onAnimationEnd(animation: Animation) {
                    holder.removePosition()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // Este método no es necesario para la animación, pero de debe implementar
                }
            })
            holder.binding.description.startAnimation(animation)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var binding: ItemHabitBinding

        init {
            itemView.setOnClickListener(this)
            binding = ItemHabitBinding.bind(itemView)
        }

        fun addPosition() {
            positionDescriptionShowed.add(layoutPosition)
            notifyDataSetChanged()
        }

        fun removePosition() {
            positionDescriptionShowed.remove(layoutPosition)
            notifyDataSetChanged()
        }

        override fun onClick(view: View) {
            if (view.isSelected) {
                view.isSelected = false
                selectedPosition = -1
            } else {
                view.isSelected = true
                selectedPosition = layoutPosition
            }
            listener.onItemClick(view, layoutPosition)
            notifyDataSetChanged()
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun updateData(data: List<Habit>?) {
        list.clear()
        list.addAll(data!!)
        orderByName()
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun undo(habit: Habit) {
        list.add(deletedPosition, habit)
        notifyItemInserted(deletedPosition)
    }
}