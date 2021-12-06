<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading" :data="list"
      :cell-style="{padding:0+'px'}"
      style="width: 1080px"
      element-loading-text="Loading" border fit highlight-current-row
    >
      <el-table-column align="center" label="类型" width="55">
        <template slot-scope="scope">
          {{ scope.row.type }}
        </template>
      </el-table-column>

      <el-table-column label="标题" min-idth="650">
        <template slot-scope="scope">

          <el-image class="cover" :src="scope.row.coverPath"
                    :preview-src-list="[scope.row.imgList]" lazy>
          </el-image>

          <ul class="nameAndTitle">
            <li>
              <div style="display: inline-block"> {{ scope.row.name }}</div>
              <div style="display: inline-block; float:right; font-size: 20px"><i class="el-icon-download"></i></div>
              <div style="display: inline-block; float:right;width: 30px; font-size: 20px"><i
                :class="starClass(scope.row)"></i></div>
            </li>
            <li> {{ scope.row.title }}</li>
            <li>
              <el-progress :percentage="showProgress(scope.row)" :format="format"></el-progress>
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
    },

    showProgress(row) {
      let d = row.downloaded ? row.downloaded : 0
      let l = row.left ? row.left : 0
      let dl = d + l
      if (dl == 0) {
        return 0
      } else {
        return Math.round(d * 1000 / dl) / 10
      }
    },
    starClass(row) {
      console.log(row.utStatus)
      return row.utStatus != undefined ? "el-icon-star-on" : "el-icon-star-off"
    },

    format(percentage) {
      return '';
    }

  }
}
</script>

<style lang="scss" scoped>

.app-container {
  text-align: center;

  .cover {
    margin: 10px;
    width: 50px;
    height: 70px;
    float: left;
  }

  li {
    list-style: none;
  }

  .nameAndTitle ul {
    float: left;

  }
}
</style>
