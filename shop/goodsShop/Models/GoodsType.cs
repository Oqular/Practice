using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class GoodsType
    {
        public long id { get; set; }
        public string type { get; set; }
        //public List<Goods> goods {get;set;} // list of goods that are of this type (Lists of {goods or goodsId})
    }
}
