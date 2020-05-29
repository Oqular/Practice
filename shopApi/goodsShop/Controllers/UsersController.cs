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
    public class UsersController : ControllerBase
    {
        private readonly GoodsContext _context;

        public UsersController(GoodsContext context)
        {
            _context = context;
        }

        // GET: api/Users
        [Authorize(Roles = "admin")]
        [HttpGet]
        public IEnumerable<User> GetUser()
        {
            return _context.user;
        }

        // GET: api/Users/5
        [Authorize]
        [HttpGet("{id}")]
        public async Task<IActionResult> GetUser([FromRoute] long id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var user = await _context.user.Include(u => u.goods).FirstOrDefaultAsync(u => u.id == id);

            if (user == null)
            {
                return NotFound();
            }

            return Ok(user);
        }

        // GET: api/Users/5/goods
        [Authorize]
        [HttpGet("{id}/goods")]
        public async Task<ActionResult<IEnumerable<Goods>>> GetUserGoodsAsync([FromRoute] long id)
        {
            var user = await _context.user.Include(u => u.goods).ThenInclude(g => g.images).FirstOrDefaultAsync(u => u.id == id);
            if (user == null)
                return NotFound();

            var goods = user.goods.ToList();

            if (goods == null)
                return NotFound();


            return goods;
        }

        // PUT: api/Users/5
        [Authorize]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutUser([FromRoute] long id, [FromBody] User user)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != user.id)
            {
                return BadRequest();
            }

            _context.Entry(user).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Users
        [HttpPost]
        public async Task<IActionResult> PostUser([FromBody] User user)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.user.Add(user);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetUser", new { id = user.id }, user);
        }

        // DELETE: api/Users/5
        [Authorize(Roles = "admin")]
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUser([FromRoute] long id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var user = await _context.user.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }

            _context.user.Remove(user);
            await _context.SaveChangesAsync();

            return Ok(user);
        }

        private bool UserExists(long id)
        {
            return _context.user.Any(e => e.id == id);
        }
    }
}