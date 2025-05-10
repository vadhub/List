package com.vlg.list.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.DateFormatter
import com.vlg.list.R
import com.vlg.list.model.Group
import com.vlg.list.model.GroupWithItems
import com.vlg.list.ui.adapter.AdapterGroup
import com.vlg.list.ui.dialog.SaveGroupDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

class GroupFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerGroup)
        val buttonAdd = view.findViewById<FloatingActionButton>(R.id.addGroupButton)

        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterGroup(
            clickOnViewListener = { group ->
                viewModel.currentGroup = group
                navigator.startFragment(SetItemsFragment())
            },
            onEditClick = { group ->
                showEditGroupDialog(group)
            },
            onDeleteClick = { group ->
                viewModel.deleteGroupWithItems(group)
            },
            onExportClick = { group ->
                exportToCsv(group)
            }
        )

        recycler.adapter = adapter
        getGroupList(adapter)

        buttonAdd.setOnClickListener {
            createDialogSaveGroup { name ->
                viewModel.saveGroup(Group(0, name, DateFormatter.getDate()))
            }
        }
    }

    fun getGroupList(adapter: AdapterGroup) {
        lifecycle.coroutineScope.launch {
            viewModel.getGroupList().collect {
                adapter.groups = it
            }
        }
    }

    fun createDialogSaveGroup(
        title: String = "Save Group",
        existingName: String = "",
        save: (String) -> Unit
    ) {
        val dialogSave = SaveGroupDialog(
            save = save,
            existingName = existingName,
            title = title
        )
        dialogSave.show(childFragmentManager, "SaveGroupDialog")
    }

    fun showEditGroupDialog(group: Group) {
        createDialogSaveGroup(
            title = "Rename Group",
            existingName = group.nameGroup,
            save = { newName ->
                if (newName.isNotBlank() && newName != group.nameGroup) {
                    val updatedGroup = group.copy(nameGroup = newName)
                    viewModel.updateGroup(updatedGroup)
                }
            }
        )
    }

    fun exportToCsv(group: Group) {
        // Implement your CSV export logic here
        // For example:
//        Toast.makeText(requireContext(), "Exporting ${group.nameGroup} to CSV", Toast.LENGTH_SHORT).show()

        lifecycle.coroutineScope.launch {
            val groupWithItems = viewModel.getGroupWithItemsById(group.id).first()
            val csvContent = buildCsvContent(groupWithItems)
            saveCsv(requireContext(), "contacts.csv", csvContent)
        }
    }

    fun generateCsvContent(data: List<List<String>>): String {
        val csv = StringBuilder()
        data.forEach { row ->
            csv.append(row.joinToString(separator = ",") { "\"${it.replace("\"", "\"\"")}\"" })
            csv.append("\n")
        }
        return csv.toString()
    }

    private fun buildCsvContent(groupWithItems: GroupWithItems): String {
        val stringBuilder = StringBuilder()

        // Add header
        stringBuilder.append("\"Group ID\",\"Group Name\",\"Created Date\"\n")
        stringBuilder.append("\"${groupWithItems.group.id}\",")
        stringBuilder.append("\"${escapeCsvField(groupWithItems.group.nameGroup)}\",")
        stringBuilder.append("\"${groupWithItems.group.dateCreated}\"\n\n")

        // Add items header
        stringBuilder.append("\"Student ID\",\"Student Count\"\"Student Name\",\"Student Date\"\n")

        // Add items
        groupWithItems.items.forEach { item ->
            stringBuilder.append("\"${item.id}\",")
            stringBuilder.append("\"${item.count}\",")
            stringBuilder.append("\"${escapeCsvField(item.name)}\",")
            stringBuilder.append("\"${item.dataChange}\"\n")
        }

        return stringBuilder.toString()
    }

    private fun escapeCsvField(field: String): String {
        return field.replace("\"", "\"\"") // Escape quotes by doubling them
    }

    fun saveCsv(context: Context, fileName: String, content: String) {
        try {
            // Use the shared Documents directory
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "")
            if (!dir.exists()) {
                dir.mkdirs() // Create the directory if it doesn't exist
            }
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName2 = "contacts_$timestamp.csv"

            val file = File(dir, fileName2)

            FileWriter(file).use { writer ->
                writer.write(content)
            }

            Toast.makeText(context, "Saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving file", Toast.LENGTH_SHORT).show()
        }
    }
}