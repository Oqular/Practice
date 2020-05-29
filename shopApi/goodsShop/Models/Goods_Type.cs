using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class Goods_Type
    {
        public long id { get; set; }
        public long goodsId { get; set; }
        public long typesId { get; set; }
    }
}
