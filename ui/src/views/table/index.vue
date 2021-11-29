<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading" :data="list"
      element-loading-text="Loading" border fit highlight-current-row
    >
      <el-table-column align="center" label="类型" width="95">
        <template slot-scope="scope">
          {{ scope.row.type }}
        </template>
      </el-table-column>
      <el-table-column label="标题" class="title">
        <template slot-scope="scope">
          <el-image
            style="width: 70px; height: 100px"
            :src="scope.row.coverPath"
            :preview-src-list="[scope.row.coverPath]">
          </el-image>
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column label="上传时间" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.uploadTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="大小" width="60" align="center">
        <template slot-scope="scope">
          {{ scope.row.size }}
        </template>
      </el-table-column>
      <el-table-column label="做种" width="60" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>

      <el-table-column label="下载" width="60" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
      <el-table-column label="完成" width="60" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
      <el-table-column label="发布者" width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {getList} from '@/api/table'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      torrentQuery: {},
      list: null,
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      //  console.log( "safasfsafas",this.$store.state.user.token)
      getList(this.torrentQuery).then(response => {
        this.list = response.body.result
        this.listLoading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .title {
    .cover {

      height: 60px;
    }
  }
}
</style>
