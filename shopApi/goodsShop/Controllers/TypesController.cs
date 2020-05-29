using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using goodsShop.Models;
using Microsoft.AspNetCore.Authorization;

namespace goodsShop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TypesController : ControllerBase
    {
        private readonly GoodsContext _context;

        public TypesController(GoodsContext context)
        {
            _context = context;
        }

        // GET: api/Types
        [HttpGet]
        public IEnumerable<Types> GetTypes()
        {
            return _context.types.Include(t => t.goods);
        }

        // GET: api/Types/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetTypes([FromRoute] long id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            SpecificType specificType = new SpecificType();

            var types = await _context.types.Include(t => t.goods).FirstOrDefaultAsync(t => t.id == id);
            specificType.id = types.id;
            specificType.name = types.name;
            specificType.specGoods = new List<Goods>();
            foreach (var gt in types.goods)
            {
                var goods = await _context.goods.Include(g => g.images).FirstOrDefaultAsync(g => g.id == gt.goodsId);
                specificType.specGoods.Add(goods);
            }

            if (types == null)
            {
                return NotFound();
            }

            return Ok(specificType);
        }

        // PUT: api/Types/5
        [Authorize(Roles = "admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutTypes([FromRoute] long id, [FromBody] Types types)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != types.id)
            {
                return BadRequest();
            }

            _context.Entry(types).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TypesExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return Ok(types);
        }

        // POST: api/Types
        [Authorize(Roles = "admin")]
        [HttpPost]
        public async Task<IActionResult> PostTypes([FromBody] Types types)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.types.Add(types);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetTypes", new { id = types.id }, types);
        }

        // DELETE: api/Types/5
        [Authorize(Roles = "admin")]
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteTypes([FromRoute] long id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var types = await _context.types.FindAsync(id);
            if (types == null)
            {
                return NotFound();
            }

            _context.types.Remove(types);
            await _context.SaveChangesAsync();

            return Ok(types);
        }

        private bool TypesExists(long id)
        {
            return _context.types.Any(e => e.id == id);
        }
    }
}