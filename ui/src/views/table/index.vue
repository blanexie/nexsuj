<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading" :data="list"
      :cell-style="{padding:0+'px'}"
      element-loading-text="Loading" border fit highlight-current-row
    >
      <el-table-column align="center" label="类型" width="55">
        <template slot-scope="scope">
          {{ scope.row.type }}
        </template>
      </el-table-column>


      <el-table-column label="标题" min-width="600">
        <template slot-scope="scope">

          <el-image class="cover" :src="scope.row.coverPath"
                    :preview-src-list="[scope.row.imgList]" lazy>
          </el-image>

          <ul class="nameAndTitle">
            <li> {{ scope.row.name }}</li>
            <li> {{ scope.row.title }}</li>
            <li>
              <el-progress :percentage="100" status="success"></el-progress>
            </li>
          </ul>
        </template>
      </el-table-column>


      <el-table-column label="上传时间" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ formatDate(scope.row.uploadTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="大小" width="70" align="center">
        <template slot-scope="scope">
          {{ showSize(scope.row.size) }}
        </template>
      </el-table-column>
      <el-table-column label="做种" width="50" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>

      <el-table-column label="下载" width="50" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
      <el-table-column label="完成" width="50" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
      <el-table-column label="发布者" width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.uploadUserName }}
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
      getList(this.torrentQuery).then(response => {
        this.list = response.body.result
        this.listLoading = false
      })
    },
    showSize: function (size) {
      return this.$showSize(size)
    },
    formatDate(millisecond) {
      let ut = this.$moment(millisecond)
      return ut.format("MM-DD hh:mm")
    }

  }
}
</script>

<style lang="scss" scoped>

.app-container {
  .table-title {
    width: 650px;
  }

  .cover {
    margin: 10px;
    width: 50px;
    height: 70px;
    float: left;
  }

  .nameAndTitle ul {
    margin: 0px 10px;
    float: left;

    li {
      margin: 2px 0px;
      list-style: none;
      white-space: nowrap; /*不让文字内容换行*/
      overflow: hidden; /*文字溢出的部分隐藏起来*/
      text-overflow: ellipsis; /*用...替代溢出的部分*/
      width: 400px;
    }
  }
}
</style>
