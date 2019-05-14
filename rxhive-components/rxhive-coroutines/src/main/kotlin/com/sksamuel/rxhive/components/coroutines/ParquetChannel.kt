package com.sksamuel.rxhive.components.coroutines

import com.sksamuel.rxhive.Struct
import com.sksamuel.rxhive.parquet.parquetWriter
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.example.Paper.schema
import org.apache.parquet.hadoop.ParquetWriter

suspend fun parquetConsumer(channel: ReceiveChannel<Struct>, path: Path, conf: Configuration) {
  var element: Struct? = channel.receiveOrNull()
  val writer: ParquetWriter<Struct> = parquetWriter(path, conf, schema)
  while (element != null) {
    writer.write(element)
    element = channel.receiveOrNull()
  }
  writer.close()
}