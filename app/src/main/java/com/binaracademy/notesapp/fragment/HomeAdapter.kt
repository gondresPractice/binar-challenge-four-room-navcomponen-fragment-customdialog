package com.binaracademy.notesapp.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binaracademy.notesapp.MainActivity
import com.binaracademy.notesapp.R

import com.binaracademy.notesapp.database.NotesDatabase
import com.binaracademy.notesapp.databinding.ItemRvNotesBinding
import com.binaracademy.notesapp.model.Notes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.w3c.dom.Text

class HomeAdapter(private val notes : List<Notes>) : RecyclerView.Adapter<HomeAdapter.MainViewHolder>() {



    class MainViewHolder( val binding : ItemRvNotesBinding): RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemRvNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MainViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.tvId.text = notes[position].id.toString()
        holder.binding.tvTitle.text = notes[position].title
        holder.binding.tvActivity.text = notes[position].description

        var title =""
        var activity =""

        val view =
            LayoutInflater.from(holder.itemView.context).inflate(R.layout.custom_dialog, null, false)
        val dialog = AlertDialog.Builder(holder.itemView.context)
        dialog.setView(view)
        dialog.setCancelable(true)

        val dialogRead = dialog.create()
        val tvHead = view.findViewById<TextView>(R.id.tvHead)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etActivity = view.findViewById<EditText>(R.id.etActivity)
        val btnOk = view.findViewById<Button>(R.id.btnOk)


        holder.binding.ivEdit.setOnClickListener{
            val db = NotesDatabase.getInstance(holder.itemView.context)
            dialogRead.show()

            tvHead.setText("Update List")
            etTitle.setText(notes[position].title)
            etActivity.setText(notes[position].description)



            btnOk.setOnClickListener{
                title = etTitle.text.toString()
                activity = etActivity.text.toString()

                val notes = Notes(
                    notes[position].id,
                    title,
                    activity
                )
                Thread {
                    val result = db?.NotesDao()?.updateNotes(notes)
                    (holder.itemView.context as MainActivity).runOnUiThread{
                        if(result!=0){
                            Toast.makeText(it.context,"Updated",Toast.LENGTH_LONG).show()
                                dialogRead.dismiss()
                        }else{
                            Toast.makeText(it.context,"Failed to updated",Toast.LENGTH_LONG).show()
                        }
                    }
                }.start()
            }



        }

        holder.binding.ivDelete.setOnClickListener{

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete!")
                .setMessage("You sure want to delete ?")
                .setCancelable(true)
                .setPositiveButton("Yes"){
                        dialog, _ ->  val db = NotesDatabase.getInstance(holder.itemView.context)

                    Thread{
                        val result = db?.NotesDao()?.deleteNotes(notes[position])

                        (holder.itemView.context as MainActivity).runOnUiThread {
                            if(result!=0){
                                Toast.makeText(it.context,"Deleted id ${notes[position].id}", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(it.context,"Failed to delete ${notes[position].id}", Toast.LENGTH_LONG).show()
                            }
                        }

                    }.start()
                }
                .setNegativeButton("No"){
                        dialog,_ -> dialog.dismiss()
                }
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)



        }
//


    }

    override fun getItemCount(): Int {
        return notes.size
    }
}